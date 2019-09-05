import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICollaborators } from 'app/shared/model/collaborators.model';
import { CollaboratorsService } from './collaborators.service';

@Component({
    selector: 'jhi-collaborators-update',
    templateUrl: './collaborators-update.component.html'
})
export class CollaboratorsUpdateComponent implements OnInit {
    collaborators: ICollaborators;
    isSaving: boolean;

    constructor(protected collaboratorsService: CollaboratorsService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ collaborators }) => {
            this.collaborators = collaborators;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.collaborators.id !== undefined) {
            this.subscribeToSaveResponse(this.collaboratorsService.update(this.collaborators));
        } else {
            this.subscribeToSaveResponse(this.collaboratorsService.create(this.collaborators));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollaborators>>) {
        result.subscribe((res: HttpResponse<ICollaborators>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
