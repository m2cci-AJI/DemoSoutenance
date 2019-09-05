/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { ProjectsUpdateComponent } from 'app/projects/projects-update.component';
import { ProjectsService } from 'app/projects/projects.service';
import { Projects } from 'app/shared/model/projects.model';

describe('Component Tests', () => {
    describe('Projects Management Update Component', () => {
        let comp: ProjectsUpdateComponent;
        let fixture: ComponentFixture<ProjectsUpdateComponent>;
        let service: ProjectsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [ProjectsUpdateComponent]
            })
                .overrideTemplate(ProjectsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProjectsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Projects(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.projects = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Projects();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.projects = entity;
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
