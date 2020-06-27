(ns contacts.core
  (:require [org.httpkit.server :refer [run-server]]
            [reitit.core :as r]
            [reitit.ring :as ring]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]
            [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                          coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.coercion.schema]
            [schema.core :as s]
            [muuntaja.core :as m]
            [contacts.contacts :refer [get-contacts
                                       create-contact
                                       get-contact-by-id
                                       update-contact
                                       delete-contact]]))

(defonce server (atom nil))

(def ping-routes
  ["/ping" {:name :ping
            :get (fn [_]
                   {:status 200
                    :body {:ping "pong"}})}])

(def contact-routes
  ["/contacts"
   ["/" {:get get-contacts
         :post {:parameters {:body {:first-name s/Str
                                    :last-name s/Str
                                    :email s/Str}}
                :handler create-contact}}]
   ["/:id" {:coercion reitit.coercion.schema/coercion
            :parameters {:path {:id s/Int}}
            :get get-contact-by-id
            :put {:parameters {:body {:first-name s/Str
                                      :last-name s/Str
                                      :email s/Str}}
                  :handler update-contact}
            :delete delete-contact}]])

(def app
  (ring/ring-handler
   (ring/router
    [["/" {:get (fn [_]
                  {:status 200
                   :body {:hello "world"}})}]
     ["/api"
      ping-routes
      contact-routes]]
    {:data {:coercion reitit.coercion.schema/coercion
            :muuntaja m/instance
            :middleware [parameters-middleware
                         format-negotiate-middleware
                         format-response-middleware
                         exception-middleware
                         format-request-middleware
                         coerce-exceptions-middleware
                         coerce-request-middleware
                         coerce-response-middleware]}})
   (ring/routes
    (ring/create-default-handler))))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main []
  (println "Server started")
  (reset! server (run-server app {:port 4000})))

(defn restart-server []
  (stop-server)
  (-main))

(comment
  contacts/routes
  (app {:request-method :get
        :uri "/api/contacts/"})
  (app {:request-method :delete
        :uri "/api/contacts/1"})
  (app {:request-method :put
        :uri "/api/contacts/3"
        :body "{\"first-name\":\"Bob\",\"last-name\":\"Dole\",\"email\":\"bob.dole@gmail.com\"}"})
  (restart-server))
