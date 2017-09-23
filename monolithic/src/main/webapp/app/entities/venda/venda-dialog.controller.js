(function() {
    'use strict';

    angular
        .module('monolithicApp')
        .controller('VendaDialogController', VendaDialogController);

    VendaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Venda'];

    function VendaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Venda) {
        var vm = this;

        vm.venda = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.venda.id !== null) {
                Venda.update(vm.venda, onSaveSuccess, onSaveError);
            } else {
                Venda.save(vm.venda, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('monolithicApp:vendaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
