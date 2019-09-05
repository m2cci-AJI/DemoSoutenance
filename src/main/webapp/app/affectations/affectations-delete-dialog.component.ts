import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAffectations } from 'app/shared/model/affectations.model';
import { AffectationsService } from './affectations.service';

@Component({
    selector: 'jhi-affectations-delete-dialog',
    templateUrl: './affectations-delete-dialog.component.html'
})
export class AffectationsDeleteDialogComponent {
    affectations: IAffectations;

    constructor(
        protected affectationsService: AffectationsService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.affectationsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'affectationsListModification',
                content: 'Deleted an affectations'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-affectations-delete-popup',
    template: ''
})
export class AffectationsDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ affectations }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AffectationsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.affectations = affectations;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popupA: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popupA: null } }]);
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
