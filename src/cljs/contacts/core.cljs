(ns contacts.core
  (:require [ajax.core :refer [GET]]
            [helix.core :refer [defnc $ <> provider]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom" :as dom]
            [contacts.state :refer [contacts-state contact-reducer]]
            [contacts.components.nav :refer [nav]]
            [contacts.components.contact-form :refer [contact-form]]
            [contacts.components.contact-list :refer [contact-list]]))

(defnc app []
  (let [[contacts set-contacts] (hooks/use-state nil)]
    (hooks/use-effect
     :once
     (GET "http://localhost:4000/api/contacts"
       {:handler #(set-contacts %)}))
    (provider {:context contacts-state
               :value (hooks/use-reducer contact-reducer 0)}
              (if contacts
                (<>
                 ($ nav)
                 (d/div {:class '[container pt-4]}
                        ($ contact-list {:contacts contacts})
                        ($ contact-form {:contact (first contacts)})))
                (d/p "Loading...")))))

(defn ^:export init []
  (dom/render
   ($ app)
   (js/document.getElementById "app")))

(comment
  (GET "http://localhost:4000/api/contacts"
    {:handler (fn [response]
                (.log js/console response))})
  (init)
  (println "hello")
  ())