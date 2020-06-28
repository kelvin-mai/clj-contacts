(ns contacts.core
  (:require [org.httpkit.server :refer [run-server]]
            [reitit.ring :as ring]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]
            [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                          coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.coercion.schema]
            [muuntaja.core :as m]
            [contacts.routes :refer [ping-routes contact-routes]]))

(defonce server (atom nil))

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
            :middleware [[wrap-cors
                          :access-control-allow-origin [#"http://localhost:4200"]
                          :access-control-allow-methods [:get :post :put :delete]]
                         parameters-middleware
                         format-negotiate-middleware
                         format-response-middleware
                         exception-middleware
                         format-request-middleware
                         coerce-exceptions-middleware
                         coerce-request-middleware
                         coerce-response-middleware]}})
   (ring/routes
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler
     {:not-found (constantly {:status 404 :body "Route not found"})}))))

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
  (app {:request-method :get
        :uri "/api/invalid"})
  (app {:request-method :get
        :uri "/api/contacts"})
  (app {:request-method :delete
        :uri "/api/contacts/1"})
  (app {:request-method :post
        :uri "/api/contacts/"
        :body "{\"first-name\":\"Kelvin\",\"last-name\":\"Mai\",\"email\":\"kelvin.mai002@gmail.com\"}"})
  (restart-server))
