package com.anggaari.basic.urlhandling;

import com.anggaari.basic.api.ApiService;
import com.anggaari.basic.sustainableclient.ServiceGenerator;

public class Main {
    public static void main(String[] args) {
        /**
         * # Good Practice
         * base url: https://futurestud.io/api/
         * endpoint: my/endpoint
         * Result:   https://futurestud.io/api/my/endpoint
         *
         * # Bad Practice
         * base url: https://futurestud.io/api
         * endpoint: /my/endpoint
         * Result:   https://futurestud.io/my/endpoint
         *
         * # Example 1
         * base url: https://futurestud.io/api/v3/
         * endpoint: my/endpoint
         * Result:   https://futurestud.io/api/v3/my/endpoint
         *
         * # Example 2
         * base url: https://futurestud.io/api/v3/
         * endpoint: /api/v2/another/endpoint
         * Result:   https://futurestud.io/api/v2/another/endpoint
         *
         * # Example 3 — completely different url
         * base url: http://futurestud.io/api/
         * endpoint: https://api.futurestud.io/
         * Result:   https://api.futurestud.io/
         *
         * # Example 4 — Keep the base url’s scheme
         * base url: https://futurestud.io/api/
         * endpoint: //api.futurestud.io/
         * Result:   https://api.futurestud.io/
         *
         * # Example 5 — Keep the base url’s scheme
         * base url: http://futurestud.io/api/
         * endpoint: //api.github.com
         * Result:   http://api.github.com
         */

        ServiceGenerator.changeApiBaseUrl("http://development.futurestud.io/api");
        ServiceGenerator.createService(ApiService.class);
    }
}
