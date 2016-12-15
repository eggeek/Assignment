set linesize 150;

spool output/test_maintain_count_output.txt

prompt --- prepare test ---

prompt    --- insert 5 test courses ---

insert into COURSES_AVAILABLE values ('INT1', 'Transport of VIPs', 1);
insert into COURSES_AVAILABLE values ('INT2', 'course2', 1);
insert into COURSES_AVAILABLE values ('INT3', 'course3', 1);
insert into COURSES_AVAILABLE values ('INT4', 'course4', 1);
insert into COURSES_AVAILABLE values ('INT5', 'course5', 1);

prompt    --- Insert 3 test drivers ---


prompt        --- insert alice ---

insert into driver values (Driver_driver_id_SEQ.nextval, 'alice', 'one', '123123', sysdate, 1, 'yes', 0);


prompt        --- insert bob ---

insert into driver values (Driver_driver_id_SEQ.nextval, 'bob', 'one', '123124', sysdate, 1, 'yes', 0);


prompt        --- insert cat ---

insert into driver values (Driver_driver_id_SEQ.nextval, 'cat', 'one', '123125', sysdate, 1, 'yes', 0);



prompt --- before insert ---;

select driver_id, first_name, QUALIFICATIONS_COUNT from driver;


prompt --- start insert ---

prompt    --- alice got INT1, INT2, INT3, INT4 and INT5 ---

insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'alice'),
  'INT1', sysdate);

insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'alice'),
  'INT2', sysdate);
  
insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'alice'),
  'INT3', sysdate);
  
insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'alice'),
  'INT4', sysdate);

insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'alice'),
  'INT5', sysdate);
  

prompt    --- bob got INT1 and INT4 ---

insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'bob'),
  'INT1', sysdate);
  
insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'bob'),
  'INT4', sysdate);
  
prompt    --- cat got INT1 ---

insert into QUALIFICATIONS values (
  (select d.driver_id from driver d where d.first_name = 'cat'),
  'INT1', sysdate);

prompt --- after insert ---

select driver_id, first_name, QUALIFICATIONS_COUNT from driver;

spool off;