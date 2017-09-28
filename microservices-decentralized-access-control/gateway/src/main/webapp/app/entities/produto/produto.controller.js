(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProdutoController', ProdutoController);

    ProdutoController.$inject = ['Produto'];

    function ProdutoController(Produto) {

        var vm = this;

        vm.produtos = [];

        loadAll();

        function loadAll() {
            Produto.query(function(result) {
                vm.produtos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
