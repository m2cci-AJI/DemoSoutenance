import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IProjects, Projects } from 'app/shared/model/projects.model';
import { ProjectsService } from './projects.service';
import { ProjectsComponent } from './projects.component';
import { ProjectsDetailComponent } from './projects-detail.component';
import { ProjectsUpdateComponent } from './projects-update.component';
import { ProjectsDeletePopupComponent } from './projects-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class ProjectsResolve implements Resolve<IProjects> {
    constructor(private service: ProjectsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProjects> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Projects>) => response.ok),
                map((projects: HttpResponse<Projects>) => projects.body)
            );
        }
        return of(new Projects());
    }
}

export const projectsRoute: Routes = [
    {
        path: 'Project',
        component: ProjectsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'chargeplanApplicationApp.projects.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Project/:id/view',
        component: ProjectsDetailComponent,
        resolve: {
            projects: ProjectsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.projects.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Project/new',
        component: ProjectsUpdateComponent,
        resolve: {
            projects: ProjectsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.projects.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Project/:id/edit',
        component: ProjectsUpdateComponent,
        resolve: {
            projects: ProjectsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.projects.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ProjectsDeletePopupComponent,
        resolve: {
            projects: ProjectsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.projects.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popupP'
    }
];
