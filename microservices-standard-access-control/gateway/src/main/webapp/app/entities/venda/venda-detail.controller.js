(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('VendaDetailController', VendaDetailController);

    VendaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Venda'];

    function VendaDetailController($scope, $rootScope, $stateParams, previousState, entity, Venda) {
        var vm = this;

        vm.venda = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:vendaUpdate', function(event, result) {
            vm.venda = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
