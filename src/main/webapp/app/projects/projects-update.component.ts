import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProjects } from 'app/shared/model/projects.model';
import { ProjectsService } from './projects.service';

@Component({
    selector: 'jhi-projects-update',
    templateUrl: './projects-update.component.html'
})
export class ProjectsUpdateComponent implements OnInit {
    projects: IProjects;
    isSaving: boolean;

    constructor(protected projectsService: ProjectsService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ projects }) => {
            this.projects = projects;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.projects.id !== undefined) {
            this.subscribeToSaveResponse(this.projectsService.update(this.projects));
        } else {
            this.subscribeToSaveResponse(this.projectsService.create(this.projects));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjects>>) {
        result.subscribe((res: HttpResponse<IProjects>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
