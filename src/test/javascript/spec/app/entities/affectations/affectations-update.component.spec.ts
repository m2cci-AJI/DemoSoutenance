/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { AffectationsUpdateComponent } from 'app/affectations/affectations-update.component';
import { AffectationsService } from 'app/affectations/affectations.service';
import { Affectations } from 'app/shared/model/affectations.model';

describe('Component Tests', () => {
    describe('Affectations Management Update Component', () => {
        let comp: AffectationsUpdateComponent;
        let fixture: ComponentFixture<AffectationsUpdateComponent>;
        let service: AffectationsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [AffectationsUpdateComponent]
            })
                .overrideTemplate(AffectationsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AffectationsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AffectationsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Affectations(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.affectations = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Affectations();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.affectations = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
