(ns contacts.utils
  (:require [clojure.string :refer [replace capitalize]]))

(def contact-form-fields
  ["first_name" "last_name" "email"])

(defn make-label-str [s]
  (str (-> s
           (replace "_" " ")
           capitalize)
       ": "))
