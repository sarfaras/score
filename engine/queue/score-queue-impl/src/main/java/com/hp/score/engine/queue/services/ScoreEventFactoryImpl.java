package com.hp.score.engine.queue.services;

import com.hp.score.api.execution.ExecutionParametersConsts;
import com.hp.score.events.EventConstants;
import com.hp.score.events.ScoreEvent;
import com.hp.score.facade.entities.Execution;
import com.hp.score.facade.services.RunningExecutionPlanService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: maromg
 * Date: 03/08/2014
 */
public class ScoreEventFactoryImpl implements ScoreEventFactory {

	@Autowired
	private RunningExecutionPlanService runningExecutionPlanService;

	public ScoreEvent createFinishedEvent(Execution execution) {
		String eventType = EventConstants.SCORE_FINISHED_EVENT;
		Serializable eventData = createFinishedEventData(execution);
		return new ScoreEvent(eventType, eventData);
	}

	private Serializable createFinishedEventData(Execution execution) {
		Map<String, Serializable> eventData = new HashMap<>();
		eventData.put(ExecutionParametersConsts.SYSTEM_CONTEXT, execution.getSystemContext());
		eventData.put(EventConstants.EXECUTION_ID_CONTEXT, execution.getExecutionId());
		eventData.put(EventConstants.EXECUTION_CONTEXT, (Serializable) execution.getContexts());
		eventData.put(EventConstants.IS_BRANCH, !StringUtils.isEmpty(execution.getSystemContext().getBranchId()));
		return (Serializable) eventData;
	}

	public ScoreEvent createFailedBranchEvent(Execution execution) {
		String eventType = EventConstants.SCORE_BRANCH_FAILURE_EVENT;
		Serializable eventData = createBranchFailureEventData(execution);
		return new ScoreEvent(eventType, eventData);
	}

	private Serializable createBranchFailureEventData(Execution execution) {
		Map<String, Serializable> eventData = new HashMap<>();
		eventData.put(ExecutionParametersConsts.SYSTEM_CONTEXT, execution.getSystemContext());
		eventData.put(EventConstants.EXECUTION_ID_CONTEXT, execution.getExecutionId());
		eventData.put(EventConstants.BRANCH_ID, execution.getSystemContext().getBranchId());
		return (Serializable) eventData;
	}

	public ScoreEvent createFailureEvent(Execution execution) {
		String eventType = EventConstants.SCORE_FAILURE_EVENT;
		Serializable eventData = createFailureEventData(execution);
		return new ScoreEvent(eventType, eventData);
	}

	private Serializable createFailureEventData(Execution execution) {
		Map<String, Serializable> eventData = new HashMap<>();
		eventData.put(ExecutionParametersConsts.SYSTEM_CONTEXT, execution.getSystemContext());
		eventData.put(EventConstants.EXECUTION_ID_CONTEXT, execution.getExecutionId());
		eventData.put(EventConstants.BRANCH_ID, execution.getSystemContext().getBranchId());
		eventData.put(ExecutionParametersConsts.RUNNING_EXECUTION_PLAN_ID, execution.getRunningExecutionPlanId());
		return (Serializable) eventData;
	}

	public ScoreEvent createNoWorkerEvent(Execution execution, Long pauseId) {
		String eventType = EventConstants.SCORE_NO_WORKER_FAILURE_EVENT;
		Serializable eventData = createNoWorkerFailureEventData(execution, pauseId);
		return new ScoreEvent(eventType, eventData);
	}

    @Override
    public ScoreEvent createFinishedBranchEvent(Execution execution) {
        String eventType = EventConstants.SCORE_FINISHED_BRANCH_EVENT;
        Serializable eventData = createFinishedEventData(execution);
        return new ScoreEvent(eventType, eventData);
    }

    private Serializable createNoWorkerFailureEventData(Execution execution, Long pauseId) {
		String flowUuid = extractFlowUuid(execution.getRunningExecutionPlanId());

		Map<String, Serializable> eventData = new HashMap<>();
		eventData.put(ExecutionParametersConsts.SYSTEM_CONTEXT, execution.getSystemContext());
		eventData.put(EventConstants.EXECUTION_ID_CONTEXT, execution.getExecutionId());
		eventData.put(EventConstants.BRANCH_ID, execution.getSystemContext().getBranchId());
		eventData.put(EventConstants.FLOW_UUID, flowUuid);
		eventData.put(EventConstants.PAUSE_ID, pauseId);
		return (Serializable) eventData;
	}

	private String extractFlowUuid(Long runningExecutionPlanId) {
		return runningExecutionPlanService.getFlowUuidByRunningExecutionPlanId(runningExecutionPlanId);
	}

}
