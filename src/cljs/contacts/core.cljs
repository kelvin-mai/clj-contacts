(ns contacts.core
  (:require [ajax.core :refer [GET]]))

(defn ^:export init []
  (println "Hello world"))

(comment
  (GET "http://localhost:4000/api/contacts"
    {:handler (fn [response]
                (.log js/console response))})
  ())