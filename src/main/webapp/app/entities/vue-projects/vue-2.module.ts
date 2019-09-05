import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { VisModule2 } from './ng2-vis';
import { ChargeplanApplicationSharedModule } from 'app/shared';
import { Vue2Component, vue2Route } from '.';
import { ReactiveFormsModule } from '@angular/forms';

const ENTITY_STATES = [...vue2Route /*...vue2PopupRoute*/];

@NgModule({
    imports: [ChargeplanApplicationSharedModule, RouterModule.forChild(ENTITY_STATES), VisModule2, ReactiveFormsModule],
    declarations: [Vue2Component],
    entryComponents: [Vue2Component],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChargeplanApplicationVue2Module {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
