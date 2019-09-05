import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Collaborators, ICollaborators } from 'app/shared/model/collaborators.model';
import { CollaboratorsService } from './collaborators.service';
import { CollaboratorsComponent } from './collaborators.component';
import { CollaboratorsDetailComponent } from './collaborators-detail.component';
import { CollaboratorsUpdateComponent } from './collaborators-update.component';
import { CollaboratorsDeletePopupComponent } from './collaborators-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class CollaboratorsResolve implements Resolve<ICollaborators> {
    constructor(private service: CollaboratorsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICollaborators> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Collaborators>) => response.ok),
                map((collaborators: HttpResponse<Collaborators>) => collaborators.body)
            );
        }
        return of(new Collaborators());
    }
}

export const collaboratorsRoute: Routes = [
    {
        path: 'Collaborator',
        component: CollaboratorsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'chargeplanApplicationApp.collaborators.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Collaborator/:id/view',
        component: CollaboratorsDetailComponent,
        resolve: {
            collaborators: CollaboratorsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.collaborators.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Collaborator/new',
        component: CollaboratorsUpdateComponent,
        resolve: {
            collaborators: CollaboratorsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.collaborators.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Collaborator/:id/edit',
        component: CollaboratorsUpdateComponent,
        resolve: {
            collaborators: CollaboratorsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.collaborators.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const collaboratorsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CollaboratorsDeletePopupComponent,
        resolve: {
            collaborators: CollaboratorsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.collaborators.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popupC'
    }
];
