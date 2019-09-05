import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Affectations, IAffectations } from 'app/shared/model/affectations.model';
import { AffectationsService } from './affectations.service';
import { AffectationsComponent } from './affectations.component';
import { AffectationsDetailComponent } from './affectations-detail.component';
import { AffectationsUpdateComponent } from './affectations-update.component';
import { AffectationsDeletePopupComponent } from './affectations-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class AffectationsResolve implements Resolve<IAffectations> {
    constructor(private service: AffectationsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAffectations> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Affectations>) => response.ok),
                map((affectations: HttpResponse<Affectations>) => affectations.body)
            );
        }
        return of(new Affectations());
    }
}

export const affectationsRoute: Routes = [
    {
        path: 'Affectation',
        component: AffectationsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'chargeplanApplicationApp.affectations.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Affectation/:id/view',
        component: AffectationsDetailComponent,
        resolve: {
            affectations: AffectationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.affectations.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Affectation/new',
        component: AffectationsUpdateComponent,
        resolve: {
            affectations: AffectationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.affectations.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'Affectation/:id/edit',
        component: AffectationsUpdateComponent,
        resolve: {
            affectations: AffectationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.affectations.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const affectationsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AffectationsDeletePopupComponent,
        resolve: {
            affectations: AffectationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.affectations.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popupA'
    }
];
