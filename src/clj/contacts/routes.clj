(ns contacts.routes
  (:require [schema.core :as s]
            [contacts.contacts :refer [get-contacts
                                       create-contact
                                       get-contact-by-id
                                       update-contact
                                       delete-contact]]))
(def ping-routes
  ["/ping" {:name :ping
            :get (fn [_]
                   {:status 200
                    :body {:ping "pong"}})}])

(def contact-routes
  ["/contacts"
   ["" {:get get-contacts
        :post {:parameters {:body {:first-name s/Str
                                   :last-name s/Str
                                   :email s/Str}}
               :handler create-contact}}]
   ["/:id" {:parameters {:path {:id s/Int}}
            :get get-contact-by-id
            :put {:parameters {:body {:first-name s/Str
                                      :last-name s/Str
                                      :email s/Str}}
                  :handler update-contact}
            :delete delete-contact}]])