package com.swia.iabuilder.parsers.ttadmiral;

import com.swia.datasets.cards.CardSystem;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.parsers.BaseArmyMarshaller;

import java.util.ArrayList;
import java.util.List;

public class TabletopAdmiralLegacyArmyMarshaller extends BaseArmyMarshaller<String, Integer> {

    private static final String BASE_URL = "http://classic.tabletopadmiral.com/imperialassault/";
    private static final int DEPLOYMENT_CODE_LEN = 30;
    private static final int COMMAND_CODE_MAX_LEN = 30;

    public TabletopAdmiralLegacyArmyMarshaller() {
        super(new TabletopAdmiralCardParser());
    }

    private List<Integer> getIds(String code) {
        final int n = 2;
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < code.length(); i += n) {
            String c = code.substring(i, i + n);
            Integer id = Integer.parseInt(c, 16);
            ids.add(id);
        }
        return ids;
    }

    private String toCode(List<Integer> ids) {
        StringBuilder code = new StringBuilder();
        for (Integer id : ids) {
            code.append(toCode(id));
        }
        return code.toString();
    }

    private String toCode(Integer id) {
        return String.format("%02x", id);
    }

    @Override
    protected List<Integer> getDeploymentIds(String code) {
        code = code.substring(0, DEPLOYMENT_CODE_LEN);
        return getIds(code);
    }

    @Override
    protected List<Integer> getCommandIds(String code) {
        final int n = Math.min(DEPLOYMENT_CODE_LEN + COMMAND_CODE_MAX_LEN, code.length());
        code = code.substring(DEPLOYMENT_CODE_LEN, n);
        return getIds(code);
    }

    @Override
    protected String toDeploymentCode(CardSystem cardSystem, List<Integer> ids) {
        StringBuilder code = new StringBuilder(toCode(ids));
        while (code.length() < DEPLOYMENT_CODE_LEN) {
            code.append('0');
        }
        return code.toString();
    }

    @Override
    public Army deserialize(String code, String name) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        code = code.replace("https://", "http://");
        if (!code.startsWith(BASE_URL)) {
            return null;
        }
        code = code.replace(BASE_URL, "");
        return super.deserialize(code, name);
    }

    @Override
    protected String toCommandCode(CardSystem cardSystem, List<Integer> ids) {
        return toCode(ids);
    }

    @Override
    protected String combine(String deploymentCards, String commandCards) {
        return deploymentCards + commandCards;
    }

    @Override
    protected boolean isValid(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        code = code.replace("https://", "http://");
        return code.startsWith(BASE_URL);
    }

    @Override
    public String serialize(Army army) {
        String code = super.serialize(army);
        if (code == null || code.isEmpty()) {
            return null;
        }
        return BASE_URL + code;
    }

}
