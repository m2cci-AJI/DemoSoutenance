import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAffectations } from 'app/shared/model/affectations.model';

@Component({
    selector: 'jhi-affectations-detail',
    templateUrl: './affectations-detail.component.html'
})
export class AffectationsDetailComponent implements OnInit {
    affectations: IAffectations;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ affectations }) => {
            this.affectations = affectations;
        });
    }

    previousState() {
        window.history.back();
    }
}
