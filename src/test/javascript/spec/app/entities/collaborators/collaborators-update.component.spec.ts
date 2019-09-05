/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { CollaboratorsUpdateComponent } from 'app/collaborators/collaborators-update.component';
import { CollaboratorsService } from 'app/collaborators/collaborators.service';
import { Collaborators } from 'app/shared/model/collaborators.model';

describe('Component Tests', () => {
    describe('Collaborators Management Update Component', () => {
        let comp: CollaboratorsUpdateComponent;
        let fixture: ComponentFixture<CollaboratorsUpdateComponent>;
        let service: CollaboratorsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [CollaboratorsUpdateComponent]
            })
                .overrideTemplate(CollaboratorsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CollaboratorsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollaboratorsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Collaborators(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.collaborators = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Collaborators();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.collaborators = entity;
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
