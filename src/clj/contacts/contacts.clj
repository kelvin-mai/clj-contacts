(ns contacts.contacts
  (:require [contacts.db :as db]))

(defn get-contacts
  [_]
  {:status 200
   :body (db/get-contacts db/db)})

(defn create-contact
  [{:keys [parameters]}]
  (let [data (:body parameters)
        created-id (db/insert-contact db/db data)]
    {:status 201
     :body (db/get-contact-by-id db/db created-id)}))

(defn get-contact-by-id
  [{:keys [parameters]}]
  (let [id (:path parameters)]
    {:status 200
     :body (db/get-contact-by-id db/db id)}))

(defn update-contact
  [{:keys [parameters]}]
  (let [id (get-in parameters [:path :id])
        body (:body parameters)
        data (assoc body :id id)]
    (db/update-contact-by-id db/db data)
    {:status 200
     :body (db/get-contact-by-id db/db {:id id})}))

(defn delete-contact
  [{:keys [parameters]}]
  (let [id (:path parameters)
        before-deleted (db/get-contact-by-id db/db id)]
    (db/delete-contact-by-id db/db id)
    {:status 200
     :body {:deleted true
            :contact before-deleted}}))