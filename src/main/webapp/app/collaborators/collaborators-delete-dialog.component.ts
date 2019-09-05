import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICollaborators } from 'app/shared/model/collaborators.model';
import { CollaboratorsService } from './collaborators.service';

@Component({
    selector: 'jhi-collaborators-delete-dialog',
    templateUrl: './collaborators-delete-dialog.component.html'
})
export class CollaboratorsDeleteDialogComponent {
    collaborators: ICollaborators;

    constructor(
        protected collaboratorsService: CollaboratorsService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.collaboratorsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'collaboratorsListModification',
                content: 'Deleted an collaborators'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-collaborators-delete-popup',
    template: ''
})
export class CollaboratorsDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ collaborators }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CollaboratorsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.collaborators = collaborators;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popupC: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popupC: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
