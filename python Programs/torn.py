
# Author: JT Hamrick
# design based off of http://chrisdianamedia.com/simplestore/
import os.path
import tornado.ioloop
import tornado.web
from tornado.options import define, options
from bson import json_util
from random import randint
from Crypto.Cipher import AES
from tornado import httpclient
import tornado.template
import MySQLdb
import uuid
import urllib
import re
import random
import magic
import hashlib, uuid
import time
import json
import math
from tornado import gen

# define values for mysql connection
define("port", default=8898, help="run on the given port", type=int)
define("mysql_host", default="127.0.0.1", help="database host")
define("mysql_port", default=3306, help="database port", type=int)
define("mysql_database", default="group11", help="database name")
define("mysql_user", default="group11", help="database user")
define("mysql_password", default="MVud5qluZhmWAbtvwZ321eskouSpP915", help="database password")

__UPLOADS__ = "static/uploads/"


class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", HomeHandler),
            (r"/details/([^/]+)", DetailsHandler),
            (r"/cart", CartHandler),
            (r"/product/add", AddToCartHandler),
            (r"/product/remove/([^/]+)", RemoveFromCartHandler),
            (r"/cart/empty", EmptyCartHandler),
            (r"/upload", UploadHandler),
            (r"/userform", UserformHandler),
            (r"/welcome/([^/]+)", WelcomeHandler),
            (r"/directory/([^/]+)", DirectoryTraversalHandler),
	    (r"/login", loginHandler),
	    (r"/profile", profileHandler),
	    (r"/signUp", signUpHandler),
	    (r"/fbHandler", fbHandler),
	    (r"/privacy", privacy),
	    (r"/confirm",ConfirmHandler),
	    (r"/thanks", ThanksHandler),
	    (r"/logout", logoutHandler)
        ]
        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
            ui_modules={"Small": SmallModule},
            xsrf_cookies=True,
            debug=True,
	    login_url="/login",
            cookie_secret="2Xs2dc.y2wqZVB,qRrnyoZuWbUTnjRBG4&uxaMYtM&r%KnpL7e"
        )
        super(Application, self).__init__(handlers, **settings)
        # Have one global connection to the store DB across all handlers
        self.myDB = MySQLdb.connect(host=options.mysql_host,
                                    port=options.mysql_port,
                                    db=options.mysql_database,
                                    user=options.mysql_user,
                                    passwd=options.mysql_password)


class BaseHandler(tornado.web.RequestHandler):
    @property
    def db(self):
        return self.application.myDB

    # if there is no cookie for the current user generate one
    def get_current_user(self):
	if not self.get_secure_cookie("User_Cookie"):
	    self.set_secure_cookie("User_Cookie", str(uuid.uuid4()), httponly=True)

class HomeHandler(BaseHandler):
    def get(self):
        # get all products in the database for the store's main page
        temp = []
        c = self.db.cursor()
        c.execute("SELECT * FROM products")
        products = c.fetchall()
        # add urlencoded string to tuple for product image link
        for k, v in enumerate(products):
            temp.append(products[k] + (urllib.quote_plus(products[k][2]),))

        authorized = self.get_secure_cookie("loggedin")
        self.render("home.html", products=tuple(temp), auth=authorized)
	cookie = self.get_secure_cookie("User_Cookie")


class signUpHandler(BaseHandler):
	def get(self):
		self.render("signUp.html")
	@gen.coroutine
	def post(self):
		c = self.db.cursor()
		#grabbing credentials from the signUp page the user entered
		sqlemail = self.get_argument("email")
		#checking to make sure an account with that email doesnt already exist
		c.execute("SELECT Email FROM users")
                stored_emails = c.fetchall()
                emailtest = False
                for itm in stored_emails:
                        if itm[0] == sqlemail:
                                emailtest = True
                                break
 		sqlname = self.get_argument("name")
 		sqlphone = self.get_argument("phone")
		pwd = self.get_argument("password")
		cpwd = self.get_argument("confirmPassword")
		addy = self.get_argument("address1")
		addy1 = self.get_argument("address2")
		city = self.get_argument("city")
		state = self.get_argument("state")
		zip = self.get_argument("zip")
		expM = self.get_argument("cardexpmonth")
		expY = self.get_argument("cardexpyear")
	        pan = self.get_argument("pan")
		cvv = self.get_argument("cvv")
		#verifying valid pan
		#certificate pinning could have prevented the holocaust
		digits = int(math.log10(int(pan)))+1
		if (not isinstance(int(pan),int)) or digits != 16:
			self.write("Sorry, you entered an invalid PAN. Please go back and enter valid information: " + '<a href="/signUp">Sign Up Page</a>')
			return
		#ensuring all form fields are properly filled out
		if len(sqlemail) < 1 or len(sqlname) < 1 or len(sqlphone) < 1 or len(pwd) < 1 or len(cpwd) < 1 or len(addy) < 1 or len(city) < 1 or len(state) < 1 or len(zip) < 1 or len(expM) < 1 or len(expY) < 1:
			 self.write("All input fields were not filled out, go back to the " + '<a href="/signUp">Sign Up Page</a>')
		elif emailtest:
			 self.write("An account already exists under that email, go back to the " + '<a href="/signUp">Sign Up Page</a>')
		else:
			#if both password and confirmation password match, create an entry in the database for an account with those credentials
			#Set the cookie for that user also
			if pwd == cpwd:
				#make a call to /issuetoken to get and store a token for this user
				url = 'http://129.244.244.168:9153/IssueHandler'
				values = {'pan': pan,
					'expM': expM,
					'expY': expY,
					'name': sqlname,
					'billzip': zip}
				body = urllib.urlencode(values)
				response = yield httpclient.AsyncHTTPClient().fetch(url,
								  		    method='POST',
					              		 		    body=body,
								  		    headers=None)

				d = json.loads(response.body)
				token = d['data'][0]

				salt = uuid.uuid4().hex
 				sqlpwd = hashlib.sha256(pwd + salt).hexdigest()
				self.set_secure_cookie("User_Cookie", str(uuid.uuid4()), httponly=True)
				cookie = self.get_secure_cookie("User_Cookie")
        			c.execute("INSERT INTO users (Name, Phone, Email, Salt, SaltedHash, Cookie, address1, address2, city, state, zip, exp_month, exp_year, token) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", [sqlname, sqlphone, sqlemail, salt, sqlpwd, cookie, addy, addy1, city, state, zip, expM, expY, token])
				self.application.myDB.commit()
				#Redirect to login!
				self.write("Sign up successful! Please sign in to complete the validation: " + '<a href="/login">Login</a>')
				self.finish()
			else:
				self.write("The passwords did not match, go back to the " + '<a href="/signUp">Sign Up Page</a>')



class loginHandler(BaseHandler):
        def get(self):
                self.render("login.html")

	def post(self):
		#getting form information for the login form the user entered in
		pwd = self.get_argument("password")
		email = self.get_argument("email")
		c = self.db.cursor()
		#checking to see if the user they are trying to login to as is already logged in
		c.execute("SELECT Cookie FROM users")
		stored_cookies = c.fetchall()
		ckie = self.get_secure_cookie("User_Cookie")
		bool = False
		for itm in stored_cookies:
			if itm[0] == ckie:
				bool = True
				break
		if bool:
			self.write("Sorry, there is already a user logged in: " + '<a href="/logout">Logout</a>')
		else:
			#get the salted hash of the email the user entered and check if it is the same salted hash produced from the password they entered
			#if it is, they typed in the right password, so they get logged in with that cookie and are redirected to the profile page
			c.execute("SELECT Salt FROM users WHERE email = %s", [self.get_argument("email")])
			salt = c.fetchone()
			if salt is None:
				self.write("Sorry Dr.Moore, that user does not exist. " + '<a href="/login">Return to Log in Page</a>')
			elif salt[0] is None:
				self.write("No user with that email, please use a valid email address")
			else:
				pwd = hashlib.sha256(pwd + salt[0]).hexdigest()
				c.execute("SELECT SaltedHash FROM users WHERE email = %s", [self.get_argument("email")])
				password = c.fetchone()
				if pwd == password[0]:
                                	c.execute("SELECT Cookie FROM users WHERE Email = %s", [email])
					ckie = c.fetchone()
					self.set_secure_cookie("User_Cookie", ckie[0])
					self.application.myDB.commit()
					self.redirect("/profile")
				else:
					self.write("Incorrect password or username combination, please try again: " + '<a href="/login"> Login </a>')


class fbHandler(BaseHandler):
	def get(self):
		self.render("signup.html")

	def post(self):
		#This is what gets called from the AJAX post in the login template with the user's facebook data
		#getting the user's facebook data
		c = self.db.cursor()
		name = self.get_argument("name")
		email = self.get_argument("email")
		fbID = self.get_argument("fbID")
		bool = True
		#add them as a user into our database and if they already exist update their info
		c.execute("REPLACE INTO users (Name, Email, Cookie, fbUser) VALUES (%s, %s, %s, %s)", [name, email, fbID, bool])
		self.set_secure_cookie("User_Cookie", fbID, httponly=True)
		self.application.myDB.commit()
		self.redirect("/profile")

class profileHandler(BaseHandler):
    def get(self):
	c = self.db.cursor()
	#Ensuring this page can only be accessed by a logged in user
	c.execute("SELECT Cookie FROM users")
        stored_cookies = c.fetchall()
        ckie = self.get_secure_cookie("User_Cookie")
        bool = False
        for itm in stored_cookies:
        	if itm[0] == ckie:
			bool = True
                        break
        if not bool:
                #self.redirect("/")
                self.write("Sorry, you must be logged in to access this page: " + '<a href="/login">Login</a>')
		return
	#Get user info based on cookie if they are logged in with that cookie
        cookie = self.get_secure_cookie("User_Cookie")
        c.execute("SELECT * FROM users WHERE Cookie = %s", [cookie])
        user_info = c.fetchall()
	self.render("profile.html", user_info=user_info)

#clears all cookies, effectively logging users out as their login status depends on their cookies
class logoutHandler(BaseHandler):
	def get(self):
		self.clear_all_cookies()
		self.redirect("/")

class privacy(BaseHandler):
	def get(self):
		self.render("privacy.html")

class DetailsHandler(BaseHandler):
    def get(self, slug):
        # get the selected product from the database
        temp = []
        # remove non numerical characters from slug
        item_number = re.findall(r'\d+', slug)
        c = self.db.cursor()
        c.execute("SELECT * \
                   FROM products p \
                   LEFT JOIN (SELECT `option`, \
                                     GROUP_CONCAT(`value`) AS `value`, \
                                     product_id \
                         FROM `product_options` \
                         WHERE `product_id` = " + item_number[0] + " \
                         GROUP BY `option`) AS o ON o.product_id = p.id \
                   WHERE p.id = " + item_number[0])
        product = c.fetchall()
        # add urlencoded string to tuple for product image link
        quoted_url = urllib.quote_plus(urllib.quote_plus(product[0][2]))
        temp.append(product[0] + (quoted_url,))

        authorized = self.get_secure_cookie("loggedin")
        self.render("details.html",
                    product=tuple(temp),
                    sku=slug,
                    auth=authorized)


class CartHandler(BaseHandler):
    def get(self):
        # get the current user's cookie
        #cookie = self.get_secure_cookie("webstore_cookie")
	cookie = self.get_secure_cookie("User_Cookie")
        # get the current user's cart based on their cookie
        c = self.db.cursor()
       	c.execute("SELECT c.item, \
                          p.price, \
                          p.name, \
                          COUNT(*) AS quantity, \
                          SUM(p.price) AS subtotal, \
                          `options`, \
                          GROUP_CONCAT(c.id) AS `id` \
                   FROM cart c \
                   INNER JOIN products p on p.id = c.item \
		   WHERE c.user_cookie = %s \
                   GROUP BY c.item, c.options", [cookie])
        products = c.fetchall()
        # calculate total and tax values for cart
        total = float(sum([x[4] for x in products]))
        count = sum([x[3] for x in products])
        tax = float("{0:.2f}".format(total * 0.08517))
        shipping = 5.27

        if not total:
            shipping = 0.00

        authorized = self.get_cookie("loggedin")
        self.render("cart.html",
                    products=products,
                    total=total,
                    count=count,
                    shipping=shipping,
                    tax=tax,
                    auth=authorized)


class AddToCartHandler(BaseHandler):
    def post(self):
        # get the product information from the details page
        id = self.get_argument("product", None)
        #cookie = self.get_secure_cookie("webstore_cookie")
	cookie = self.get_secure_cookie("User_Cookie")
	product_options = ",".join(self.get_arguments("option"))
        # add the product to the user's cart
        c = self.db.cursor()
        c.execute("INSERT INTO cart (id, user_cookie, item, options) \
                   VALUES (0, '"+cookie+"', "+id+", '"+product_options+"')")
        self.application.myDB.commit()
        self.redirect("/cart")


class RemoveFromCartHandler(BaseHandler):
    def get(self, slug):
        # get the current user's cookie
	#cookie = self.get_secure_cookie("webstore_cookie")
        cookie = self.get_secure_cookie("User_Cookie")
	# use that cookie to remove selected item from the user's cart
        c = self.db.cursor()
        c.execute("DELETE FROM cart \
                   WHERE user_cookie = '" + cookie + "'  \
                       AND id IN(" + slug + ")")
        self.application.myDB.commit()
        self.redirect("/cart")


class EmptyCartHandler(BaseHandler):
    def get(self):
        # get the current user's cookie
	#cookie = self.get_secure_cookie("webstore_cookie")
	cookie = self.get_secure_cookie("User_Cookie")
        # use that cookie to remove all items from user's cart
        c = self.db.cursor()
        c.execute("DELETE FROM cart WHERE user_cookie = %s", [cookie])
        self.application.myDB.commit()
        self.redirect("/cart")


class ConfirmHandler(BaseHandler):
	def get(self):
		c = self.db.cursor()
        	#Ensuring this page can only be accessed by a logged in user
        	c.execute("SELECT Cookie FROM users")
        	stored_cookies = c.fetchall()
        	ckie = self.get_secure_cookie("User_Cookie")
        	bool = False
        	for itm in stored_cookies:
                	if itm[0] == ckie:
                        	bool = True
                	        break
      	 	if not bool:
        	        self.write("Sorry, you must be logged in to complete a purchase: " + '<a href="/login">Login</a>')
	                return

		#showing total for the confirm page
		cookie = self.get_secure_cookie("User_Cookie")
		c.execute("SELECT item FROM cart WHERE user_cookie = %s", [ckie])
                products = c.fetchall()
                orderData = list()
                total = 0.00
                for itm in products:
                        for info in itm:
                                c.execute("SELECT price FROM products WHERE id = %s", [info])
                                price = c.fetchone()
                                price = price[0]
                                total = float(price) + total

	        tax = float("{0:.2f}".format(total * 0.08517))
		total = total + tax
		if total > 0:
                	total = total + 5.27

		#getting appropriate data for confirm page
		c.execute("SELECT * FROM users WHERE Cookie = %s", [cookie])
		data = c.fetchall()
		user_data = list()
		user_data.append(total)
		count = 0
		for itm in data:
			for info in itm:
				user_data.append(info)
		userauth = self.current_user if self.current_user else None
		self.render("confirm.html", auth=userauth, user_data=user_data)

	@gen.coroutine
	def post(self):
		tpan = self.get_argument("tpan")
		exp_month = self.get_argument("exp_month")
		exp_year = self.get_argument("exp_year")
		name = self.get_argument("name")
		zip = self.get_argument("zip")
		total = self.get_argument("amount")
		cvv = self.get_argument("cvv")
		if float(total) < 5.27:
			self.write("You have not added anything to your cart yet silly! Please return to add stuff: " + '<a href="/"> Home </a>')
			return
		#sending credentials to /paytoken
		url = "http://129.244.244.168:9889/paytoken"
		values = {'tpan': tpan, 'exp_month': exp_month, 'exp_year': exp_year, 'name': name, 'billing_zip': zip, 'cvv': cvv, 'amount': total, 'token_vault': 'http://129.244.244.168:9153/getpan'}
		body = urllib.urlencode(values)
		response = yield httpclient.AsyncHTTPClient().fetch(url, method='POST', body=body, headers=None)
		d = json.loads(response.body)
		if d['status'] == 'success':
			self.redirect("/thanks")
		else:
			self.redirect("/confirm")

class ThanksHandler(BaseHandler):
	def get(self):
                c = self.db.cursor()
		#creating order number
                ckie = self.get_secure_cookie("User_Cookie")
		c.execute("SELECT token, email FROM users WHERE Cookie = %s", [ckie])
	        tokenANDemail = c.fetchall()
		token = tokenANDemail[0][0]
		email = tokenANDemail[0][1]
		peamis = 4.5
		orderNumber = random.randint(1,999999999)
		#getting appropriate order information
		c.execute("SELECT item FROM cart WHERE user_cookie = %s", [ckie])
		products = c.fetchall()
		orderData = list()
                total = 0.00
		for itm in products:
     	        	for info in itm:
        	        	c.execute("SELECT price FROM products WHERE id = %s", [info])
                                price = c.fetchone()
                                price = price[0]
				total = float(price) + total

 	    	total = float("{0:.2f}".format(total * 0.08517)) + total
		total = total + 5.27

		#inserting the purchased items into the orders table
		for itm in products:
                	for info in itm:
				c.execute("SELECT price FROM products WHERE id = %s", [info])
				price = c.fetchone()
				price = price[0]
				c.execute("INSERT INTO Orders (order_number, timestamp, customer_id, item_id, item_price, order_total, payment_token) VALUES (%s, CURRENT_TIME(), %s, %s, %s, %s, %s)", [orderNumber, email, info, price, total , token])
				self.application.myDB.commit()
		#deleting products from user's cart after they purchase it
		c.execute("DELETE FROM cart WHERE user_cookie = %s", [ckie])
		self.render("thanks.html")



class WelcomeHandler(BaseHandler):
    def get(self, name):
        TEMPLATE = open("templates/welcome.html").read()
	if "{" not in name:
		 template_data = TEMPLATE.replace("FOO", name)
		 t = tornado.template.Template(template_data)
		 self.write(t.generate(name=name))
	else:
		self.write("SORRY, illegal input")


class UserformHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("fileuploadform.html")


class UploadHandler(tornado.web.RequestHandler):
    def post(self):
        fileinfo = self.request.files['filearg'][0]
        fname = fileinfo['filename']
        # extn = os.path.splitext(fname)[1]
        # cname = str(uuid.uuid4()) + extn
        fh = open(__UPLOADS__ + fname, 'w')
        fh.write(fileinfo['body'])
        self.finish(fname + " is uploaded!! Check %s folder" % __UPLOADS__)
        # self.write(fileinfo)


class DirectoryTraversalHandler(BaseHandler):
    def get(self, slug):
        mime = magic.Magic(mime=True)
        filename = urllib.unquote(urllib.unquote(slug))
	mime_type = mime.from_file(filename)
	if (mime_type == "image/jpeg") or (mime_type == "image/png"):
		self.set_header('Content-Type', mime_type)
		with open(filename) as f:
			self.write(f.read())
	else:
		self.write("SORRY, you do not have access. ERROR: ILLEGAL ACCESS")


class SmallModule(tornado.web.UIModule):
    def render(self, item):
        return self.render_string("modules/small.html", item=item)


def main():
    http_server = tornado.httpserver.HTTPServer(Application(), ssl_options={ "certfile": os.path.join("certs/host.cert"), "keyfile": os.path.join("certs/host.key"), })
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.current().start()


if __name__ == "__main__":
    main()
