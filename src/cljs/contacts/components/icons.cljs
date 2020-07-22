(ns contacts.components.icons
  (:require [helix.core :refer [defnc]]
            [helix.dom :as d]))

;;https://heroicons.dev/

;; <svg fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path></svg>
(defnc user-icon []
  (d/svg {:fill "currentColor"
          :viewBox "0 0 20 20"
          :height "1rem"
          :width "1rem"
          :class "inline"}
         (d/path {:fill-rule "evenodd"
                  :d "M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
                  :clip-rule "evenodd"})))