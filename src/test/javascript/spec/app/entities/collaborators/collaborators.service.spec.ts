/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import { CollaboratorsService } from 'app/collaborators/collaborators.service';
import { Collaborators, ICollaborators, Skill } from 'app/shared/model/collaborators.model';

describe('Service Tests', () => {
    describe('Collaborators Service', () => {
        let injector: TestBed;
        let service: CollaboratorsService;
        let httpMock: HttpTestingController;
        let elemDefault: ICollaborators;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(CollaboratorsService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Collaborators(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', Skill.JAVA);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Collaborators', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Collaborators(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Collaborators', async () => {
                const returnedFromService = Object.assign(
                    {
                        nomCollaborator: 'BBBBBB',
                        prenomCollaborator: 'BBBBBB',
                        trigramme: 'BBBBBB',
                        email: 'BBBBBB',
                        competencies: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Collaborators', async () => {
                const returnedFromService = Object.assign(
                    {
                        nomCollaborator: 'BBBBBB',
                        prenomCollaborator: 'BBBBBB',
                        trigramme: 'BBBBBB',
                        email: 'BBBBBB',
                        competencies: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Collaborators', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
