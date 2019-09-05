import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { ChargeplanApplicationSharedModule } from 'app/shared';
import {
    AffectationsComponent,
    AffectationsDeleteDialogComponent,
    AffectationsDeletePopupComponent,
    AffectationsDetailComponent,
    affectationsPopupRoute,
    affectationsRoute,
    AffectationsUpdateComponent
} from './';

const ENTITY_STATES = [...affectationsRoute, ...affectationsPopupRoute];

@NgModule({
    imports: [ChargeplanApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AffectationsComponent,
        AffectationsDetailComponent,
        AffectationsUpdateComponent,
        AffectationsDeleteDialogComponent,
        AffectationsDeletePopupComponent
    ],
    entryComponents: [
        AffectationsComponent,
        AffectationsUpdateComponent,
        AffectationsDeleteDialogComponent,
        AffectationsDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChargeplanApplicationAffectationsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
