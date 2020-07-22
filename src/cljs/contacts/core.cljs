(ns contacts.core
  (:require [ajax.core :refer [GET]]
            [helix.core :refer [defnc $ <> provider]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom" :as dom]
            [contacts.state :refer [app-state app-reducer initial-state use-app-state]]
            [contacts.components.nav :refer [nav]]
            [contacts.components.contact-form :refer [contact-form]]
            [contacts.components.contact-list :refer [contact-list]]))

(defnc app []
  (let [[state actions] (use-app-state)]
    (hooks/use-effect
     :once
     (GET "http://localhost:4000/api/contacts"
       {:handler (:init actions)}))
    (if (:contacts state)
      (<>
       ($ nav)
       (d/div {:class '[container pt-4]}
              ($ contact-list)
              ($ contact-form)))
      (d/p "Loading..."))))

(defnc provided-app []
  (provider {:context app-state
             :value (hooks/use-reducer app-reducer initial-state)}
            ($ app)))

(defn ^:export ^:dev/after-load init []
  (dom/render
   ($ provided-app)
   (js/document.getElementById "app")))

(comment
  (GET "http://localhost:4000/api/contacts"
    {:handler (fn [response]
                (.log js/console response))})
  (init)
  (println "hello")
  ())