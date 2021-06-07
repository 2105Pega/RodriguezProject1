CREATE OR replace FUNCTION default_new_role() RETURNS TRIGGER AS
$$
BEGIN INSERT INTO "user_role"("role_id","user_id") VALUES (1,NEW."id");
RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';
--
CREATE TRIGGER new_user_trigger AFTER INSERT ON "end_user" FOR EACH ROW EXECUTE PROCEDURE default_new_role();
--
create or replace function new_account() returns trigger as 
$$
begin insert into "account"("app_id","balance","status") values (new."id",new."initialamount","p");
return new;
end;
$$
LANGUAGE 'plpgsql';
--
create trigger new_application_trigger after insert on "application" for each row execute procedure new_account();