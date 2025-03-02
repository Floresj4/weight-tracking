# Weight Tracking API

Database interactions by request routes.

<br/>

## Execution

List the options available for execution.

`python weights-api.py --help`

E.g.,

```
python weights-api.py /new --body 'abc,2025-01-01,23' 
python weights-api.py /year/2025?guid=abc --indent 4
python weights-api.py /year/2022/month/02?guid=abc --indent 4
```

## Packaging

Creating the zip archive to deploy to S3.

`zip -qjr weights-api.zip ./api/weights-api.py`


## Deployment

Update the archive to S3.

`aws s3 cp ./weights-api.zip s3://<bucket>/<prefix>`

Create the cloudformation stack with IAM role and Lambda function.

`aws cloudformation create-stack --stack-name weights-tracking-stack --template-body file://api/cloudformation/template.yml --parameters ParameterKey=S3BucketName,ParameterValue=<bucket> ParameterKey=S3Key,ParameterValue=<prefix> --capabilities CAPABILITY_NAMED_IAM`

`aws cloudformation update-stack --stack-name weights-tracking-stack --template-body file://api/cloudformation/template.yml --parameters ParameterKey=S3BucketName,ParameterValue=<bucket> ParameterKey=S3Key,ParameterValue=<prefix> --capabilities CAPABILITY_NAMED_IAM`

## Update(s)

aws lambda update-function-code --function-name weights-lambda --s3-bucket <bucket> --s3-key <prefix>

<br/>

- [Weight Tracking](../)
- [Batch](../batch/)
- [UI](../ui/)


