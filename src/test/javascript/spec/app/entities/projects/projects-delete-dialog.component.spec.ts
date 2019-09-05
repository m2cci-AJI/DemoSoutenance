/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { ProjectsDeleteDialogComponent } from 'app/projects/projects-delete-dialog.component';
import { ProjectsService } from 'app/projects/projects.service';

describe('Component Tests', () => {
    describe('Projects Management Delete Component', () => {
        let comp: ProjectsDeleteDialogComponent;
        let fixture: ComponentFixture<ProjectsDeleteDialogComponent>;
        let service: ProjectsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [ProjectsDeleteDialogComponent]
            })
                .overrideTemplate(ProjectsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProjectsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
