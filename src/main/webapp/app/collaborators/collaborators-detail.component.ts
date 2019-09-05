import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollaborators } from 'app/shared/model/collaborators.model';

@Component({
    selector: 'jhi-collaborators-detail',
    templateUrl: './collaborators-detail.component.html'
})
export class CollaboratorsDetailComponent implements OnInit {
    collaborators: ICollaborators;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ collaborators }) => {
            this.collaborators = collaborators;
        });
    }

    previousState() {
        window.history.back();
    }
}
