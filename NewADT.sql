create or replace directory blob_dir as 'C:\oracle\';

-- Create a new ADT named personalDetails
CREATE OR REPLACE TYPE personalDetails AS OBJECT (
    mobile_no VARCHAR2(10),
    photo BLOB
);

create table student(
    id INT PRIMARY KEY,
    name VARCHAR(30),
    marks INT,
    details personalDetails
);

-- Inserting personal information
INSERT INTO student (id, name, marks, details)
VALUES (
    1, 'Aryan Singh', 78,
    personalDetails('1234567890', to_blob(bfilename('BLOB_DIR', 'photo1.jpg')))
);

INSERT INTO student (id, name, marks, details)
VALUES (
    2, 'Rohit Sharma', 82,
    personalDetails('9087654321', to_blob(bfilename('BLOB_DIR', 'photo2.jpg')))
);

select s.id, s.name, s.marks, s.details.mobile_no as mobile_no ,s.details.photo as photo from student s;

