(ns contacts.contacts
  (:require [contacts.db :as db]))

(defn get-contacts
  [_]
  (println "Changed again")
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
  (let [id (:path parameters)
        contact (db/get-contact-by-id db/db id)]
    (if contact
      {:status 200
       :body contact}
      {:status 404
       :body {:error "Contact not found"}})))

(defn update-contact
  [{:keys [parameters]}]
  (let [id (get-in parameters [:path :id])
        body (:body parameters)
        data (assoc body :id id)
        updated-count (db/update-contact-by-id db/db data)]
    (if (= 1 updated-count)
      {:status 200
       :body {:updated true
              :contact (db/get-contact-by-id db/db {:id id})}}
      {:status 404
       :body {:updated false
              :error "Unable to update contact"}})))

(defn delete-contact
  [{:keys [parameters]}]
  (let [id (:path parameters)
        before-deleted (db/get-contact-by-id db/db id)
        deleted-count (db/delete-contact-by-id db/db id)]
    (if (= 1 deleted-count)
      {:status 200
       :body {:deleted true
              :contact before-deleted}}
      {:status 404
       :body {:deleted false
              :error "Unable to delete contact"}})))

(println "hello")