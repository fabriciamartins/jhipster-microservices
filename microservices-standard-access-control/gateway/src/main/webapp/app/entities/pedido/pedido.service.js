(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Pedido', Pedido);

    Pedido.$inject = ['$resource'];

    function Pedido ($resource) {
        var resourceUrl =  'pedido/' + 'api/pedidos/:id';

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
            'update': { method:'PUT' },
            'getDescricao': {
                url: 'pedido/' + 'api/pedidos/descricao/:id',
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        var response = {};
                        response.data = data;
                    }
                    return response;
                }
            }
        });
    }
})();
