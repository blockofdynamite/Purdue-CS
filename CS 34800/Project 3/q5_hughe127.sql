CREATE TRIGGER BadStanding
    BEFORE UPDATE OF Salary on Earnings
    FOR EACH ROW
    DECLARE
        LocalStanding VARCHAR2(500);
    BEGIN
        SELECT UNIQUE LOWER(Standing) INTO LocalStanding FROM Name WHERE :NEW.Email = Name.Email;
        IF (:NEW.Salary > :OLD.Salary AND LocalStanding NOT IN ('excellent', 'good')) THEN
            :NEW.Salary := :OLD.Salary;
        END IF;
    END;
/

CREATE TRIGGER TooMuchSalary
    BEFORE UPDATE OF Salary on Earnings
    FOR EACH ROW
    BEGIN
        IF (:NEW.Salary > 2000) THEN
            :NEW.Salary := 2000;
        END IF;
    END;
/
