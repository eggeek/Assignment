create or replace trigger maintain_count
before insert or delete or update on qualifications
for each row

begin
  if inserting then
    update DRIVER
      set QUALIFICATIONS_COUNT = QUALIFICATIONS_COUNT + 1
      where DRIVER_ID = :new.Driver_driver_id;
  end if;
  
  if deleting then
    update DRIVER
      set QUALIFICATIONS_COUNT = QUALIFICATIONS_COUNT - 1
      where DRIVER_ID = :old.Driver_driver_id;
  end if;
  
  if updating then
    update DRIVER
      set QUALIFICATIONS_COUNT = QUALIFICATIONS_COUNT - 1
      where DRIVER_ID = :old.Driver_driver_id;
    
    update DRIVER
      set QUALIFICATIONS_COUNT = QUALIFICATIONS_COUNT + 1
      where DRIVER_ID = :new.Driver_driver_id;
  end if;
end;