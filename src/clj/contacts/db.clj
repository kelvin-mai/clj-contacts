(ns contacts.db
  (:require [hugsql.core :as hugsql]))

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/clj_contacts"
   :user "postgres"
   :password "postgres"})

(hugsql/def-db-fns "sql/contacts.sql")

(comment
  (create-contacts-table db)
  (get-contacts db)
  (get-contact-by-id db {:id 2})
  (update-contact-by-id db {:id 3
                            :first-name "Mary"
                            :last-name "Sue"
                            :email "marysue@gmail.com"})
  (delete-contact-by-id db {:id 3}))