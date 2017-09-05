(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PedidoDetailController', PedidoDetailController);

    PedidoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pedido'];

    function PedidoDetailController($scope, $rootScope, $stateParams, previousState, entity, Pedido) {
        var vm = this;

        vm.pedido = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:pedidoUpdate', function(event, result) {
            vm.pedido = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
