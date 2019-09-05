import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjects } from 'app/shared/model/projects.model';

@Component({
    selector: 'jhi-projects-detail',
    templateUrl: './projects-detail.component.html'
})
export class ProjectsDetailComponent implements OnInit {
    projects: IProjects;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projects }) => {
            this.projects = projects;
        });
    }

    previousState() {
        window.history.back();
    }
}
