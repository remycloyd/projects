-- create table classes (
-- 	grade varchar(1),
--     credits int(1),
--     name varchar(44),
--     primary key(name)
-- 	);
--     
    -- A's
    
-- insert into classes(name, grade, credits)
-- 	values("Data Structures", "A", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("CPL", "A", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("SSW 1", "A", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("SSW 2", "A", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Information Security", "A", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Game Program", "A", 3 );   
--     

--     
--     -- B's
--     

-- insert into classes(name, grade, credits)
-- 	values("Digital Lab", "B", 1 );

-- insert into classes(name, grade, credits)
-- 	values("Assembler", "B", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Database System", "B", 3 );

-- insert into classes(name, grade, credits)
-- 	values("Prog Prob Solving", "B", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Data Structures", "B", 3 );


-- C's

    
-- insert into classes(name, grade, credits)
-- 	values("Cyber Sec", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Digital Systems", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Operating Sys", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Discrete Math", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Algorithms", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Networks", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Animation", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Ethics", "C", 3 );
--     
-- insert into classes(name, grade, credits)
-- 	values("Security Privacy", "C", 3 );

-- ALTER TABLE CLASSES add gradepoints int(2)

-- Alter table classes DROP column Gradepoints



-- INSERT INTO table_name(column1,column2,column3,…) 
-- Select value1,value2,value3,… 
-- From dual WHERE [conditional predicate];

-- insert into Classes (Gradepoints)
-- select 12
-- where ( 
-- select gradepoints
-- from classes
-- where credits = 3 and grade = "A") ;



UPDATE classes
SET    gradepoints = 12
where (grade = "A" and credits = 3);











