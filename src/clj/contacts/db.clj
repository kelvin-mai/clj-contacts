(ns contacts.db
  (:require [hugsql.core :as hugsql]))

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/clj_contacts"
   :user "postgres"
   :password "postgres"})

(hugsql/def-db-fns "sql/contacts.sql")
