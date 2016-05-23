# --- !Ups

create table "USER" ("UID" VARCHAR NOT NULL PRIMARY KEY,"TOKEN" VARCHAR NOT NULL);

# --- !Downs

drop table "CAT";
