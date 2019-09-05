import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { ChargeplanApplicationSharedModule } from 'app/shared';
import { ChargeplanApplicationCoreModule } from 'app/core';
import { ChargeplanApplicationAppRoutingModule } from './app-routing.module';
import { ChargeplanApplicationAccountModule } from './account/account.module';
import { ChargeplanApplicationEntityModule } from './entities/entity.module';
import { ChargeplanApplicationProjectsModule } from './projects/projects.module';
import { ChargeplanApplicationCollaboratorsModule } from './collaborators/collaborators.module';
import { ChargeplanApplicationAffectationsModule } from './affectations/affectations.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent } from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000,
            i18nEnabled: true,
            defaultI18nLang: 'fr'
        }),
        ChargeplanApplicationSharedModule.forRoot(),
        ChargeplanApplicationCoreModule,
        ChargeplanApplicationAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        ChargeplanApplicationEntityModule,
        ChargeplanApplicationProjectsModule,
        ChargeplanApplicationCollaboratorsModule,
        ChargeplanApplicationAffectationsModule,
        ChargeplanApplicationAppRoutingModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class ChargeplanApplicationAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
