import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { ChargeplanApplicationSharedModule } from 'app/shared';
import {
    CollaboratorsComponent,
    CollaboratorsDeleteDialogComponent,
    CollaboratorsDeletePopupComponent,
    CollaboratorsDetailComponent,
    collaboratorsPopupRoute,
    collaboratorsRoute,
    CollaboratorsUpdateComponent
} from './';

const ENTITY_STATES = [...collaboratorsRoute, ...collaboratorsPopupRoute];

@NgModule({
    imports: [ChargeplanApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CollaboratorsComponent,
        CollaboratorsDetailComponent,
        CollaboratorsUpdateComponent,
        CollaboratorsDeleteDialogComponent,
        CollaboratorsDeletePopupComponent
    ],
    entryComponents: [
        CollaboratorsComponent,
        CollaboratorsUpdateComponent,
        CollaboratorsDeleteDialogComponent,
        CollaboratorsDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChargeplanApplicationCollaboratorsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
