create or replace trigger maintain_count
after insert or delete on qualifications
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
        where driver_id = :old.driver_driver_id;
        
  end if;
end;