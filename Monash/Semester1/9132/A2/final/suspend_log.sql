CREATE OR REPLACE TRIGGER SUS_AUDIT_TRIGGER
before update of availability on driver
for each row
begin
  IF (:new.availability != :old.availability and :new.availability = 'sus' )
  then
  INSERT INTO driver_audit
  VALUES (
    driver_audit_log_id_SEQ.nextval,
    to_date(SYSDATE, 'DD-MM-YYYY HH24:MI:SS'),
    'suspended driver, id: ' || :old.driver_id, USER);
  end if;
end;