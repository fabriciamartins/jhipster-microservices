(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProdutoDetailController', ProdutoDetailController);

    ProdutoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Produto'];

    function ProdutoDetailController($scope, $rootScope, $stateParams, previousState, entity, Produto) {
        var vm = this;

        vm.produto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:produtoUpdate', function(event, result) {
            vm.produto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
