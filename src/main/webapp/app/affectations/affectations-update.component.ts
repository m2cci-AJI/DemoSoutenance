import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAffectations } from 'app/shared/model/affectations.model';
import { AffectationsService } from './affectations.service';
import { ICollaborators } from 'app/shared/model/collaborators.model';
import { CollaboratorsService } from 'app/collaborators';
import { IProjects } from 'app/shared/model/projects.model';
import { ProjectsService } from 'app/projects';

@Component({
    selector: 'jhi-affectations-update',
    templateUrl: './affectations-update.component.html'
})
export class AffectationsUpdateComponent implements OnInit {
    affectations: IAffectations;
    isSaving: boolean;

    collaborators: ICollaborators[];

    projects: IProjects[];
    dateDebutDp: any;
    dateFinDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected affectationsService: AffectationsService,
        protected collaboratorsService: CollaboratorsService,
        protected projectsService: ProjectsService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ affectations }) => {
            this.affectations = affectations;
        });
        this.collaboratorsService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICollaborators[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICollaborators[]>) => response.body)
            )
            .subscribe((res: ICollaborators[]) => (this.collaborators = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.projectsService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProjects[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProjects[]>) => response.body)
            )
            .subscribe((res: IProjects[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.affectations.id !== undefined) {
            this.subscribeToSaveResponse(this.affectationsService.update(this.affectations));
        } else {
            this.subscribeToSaveResponse(this.affectationsService.create(this.affectations));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAffectations>>) {
        result.subscribe((res: HttpResponse<IAffectations>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCollaboratorsById(index: number, item: ICollaborators) {
        return item.id;
    }

    trackProjectsById(index: number, item: IProjects) {
        return item.id;
    }
}
