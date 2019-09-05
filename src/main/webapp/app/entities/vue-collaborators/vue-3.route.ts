import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Vue2 } from 'app/shared/model/vue-2.model';
import { Vue3Service } from './vue-3.Collaborator.service';
import { Vue3Component } from './vue-3.component';
import { IVue2 } from 'app/shared/model/vue-2.model';

@Injectable({ providedIn: 'root' })
export class Vue3Resolve implements Resolve<IVue2> {
    constructor(private service: Vue3Service) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVue2> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Vue2>) => response.ok),
                map((vue2: HttpResponse<Vue2>) => vue2.body)
            );
        }
        return of(new Vue2());
    }
}

export const vue3Route: Routes = [
    {
        path: '',
        component: Vue3Component,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.vue2.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
