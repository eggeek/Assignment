set linesize 150

spool output/test_suspend_log_output.txt

prompt --- prepare test ---


prompt    --- Insert 3 test drivers ---

prompt        --- insert alice ---

insert into driver values (Driver_driver_id_SEQ.nextval, 'alice', 'one', '111111', sysdate, 1, 'yes', 0);


prompt        --- insert bob ---

insert into driver values (Driver_driver_id_SEQ.nextval, 'bob', 'two', '222222', sysdate, 1, 'yes', 0);


prompt        --- insert cat ---

insert into driver values (Driver_driver_id_SEQ.nextval, 'cat', 'three', '333333', sysdate, 1, 'yes', 0);



prompt --- before update ---

select LOG_DATETIME as datetime, LOG_USER as "USER", LOG_CONTENT as CONTENT from driver_audit;


prompt --- updating ---

prompt  --- alice availability become 'sus' ---

update driver
  set availability = 'sus'
  where lincence_number = '111111';
  
prompt  --- bob availability become 'no' ---

update driver
  set availability = 'no'
  where lincence_number = '222222';
  
prompt  --- after update ---

select LOG_DATETIME as "TIME", LOG_USER as "USER", LOG_CONTENT as "DETAIL" from driver_audit;

spool off;