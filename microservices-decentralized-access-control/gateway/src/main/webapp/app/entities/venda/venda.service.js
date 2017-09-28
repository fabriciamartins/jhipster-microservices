(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Venda', Venda);

    Venda.$inject = ['$resource'];

    function Venda ($resource) {
        var resourceUrl =  'api/vendas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
