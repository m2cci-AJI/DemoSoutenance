import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Vue2 } from 'app/shared/model/vue-2.model';
import { Vue2Service } from './vue-2.Project.service';
import { Vue2Component } from './vue-2.component';
import { IVue2 } from 'app/shared/model/vue-2.model';

@Injectable({ providedIn: 'root' })
export class Vue2Resolve implements Resolve<IVue2> {
    constructor(private service: Vue2Service) {}

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

export const vue2Route: Routes = [
    {
        path: '',
        component: Vue2Component,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chargeplanApplicationApp.vue2.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
