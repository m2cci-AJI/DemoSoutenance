import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProjects } from 'app/shared/model/projects.model';
import { ProjectsService } from './projects.service';

@Component({
    selector: 'jhi-projects-delete-dialog',
    templateUrl: './projects-delete-dialog.component.html'
})
export class ProjectsDeleteDialogComponent {
    projects: IProjects;

    constructor(protected projectsService: ProjectsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'projectsListModification',
                content: 'Deleted an projects'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-projects-delete-popup',
    template: ''
})
export class ProjectsDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projects }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ProjectsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.projects = projects;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popupP: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popupP: null } }]);
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
